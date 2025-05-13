require 'nokogiri'
require 'mysql2'
require 'dotenv/load'

# 컬럼 타입에 따른 리터럴 값을 설정한다
def dummy_value_for(mysql_type)
  case mysql_type
  when /int/, /bigint/ then "1"
  when /bool/ then "true"
  when /char/, /text/, /varchar/ then "'sample'"
  when /date/, /time/ then "'2024-01-01 00:00:00'"
  when /decimal/, /float/, /double/ then "1.23"
  else "null"
  end
end

# DB 테이블 스키마 파일로부터 컬럼별 필드를 알아낸다
def fetch_column_types(table_name)
  db_config = {
    host: "localhost",
    username: ENV['MYSQL_USERNAME'],
    password: ENV['MYSQL_PASSWORD'],
    database: "genidxpage"
  }

  client = Mysql2::Client.new(db_config)
  table_def = client.query("DESCRIBE #{table_name}")
  types = {}
  table_def.each do |col|
    types[col["Field"]] = col["Type"]
  end
  types
end

def find_table_name(doc)
  doc.xpath("//insert | //update | //select | //delete").map do |node|
    raw_sql = node.text.strip

    if raw_sql =~ /\b(?:INSERT\s+INTO|UPDATE|FROM)\s+[`"]?(\w+)[`"]?/i
      return $1
    end
  end
end

# XML에서 SQL 추출 후 리터럴을 대입한다
def extract_and_substitute(xml_path)
  doc = Nokogiri::XML(File.read(xml_path))

  table_name = find_table_name(doc)

  types = fetch_column_types(table_name)

  sqls = doc.xpath("//insert | //update | //select | //delete").map do |node|
    raw_sql = node.text.strip

    substituted_sql = raw_sql.gsub(/^(.*)(#\{(\w+)})(.*)$/) do
      left = $1
      camel_param = $3
      right = $4
      snake_param = camel_param.gsub(/(.)([A-Z])/,'\1_\2').downcase!

      mysql_type = snake_param == "" ? types[camel_param] : types[snake_param]
      sub = dummy_value_for(mysql_type || "varchar") # fallback
      "#{left}#{sub}#{right}"
    end
    substituted_sql
  end

  sqls
end

mapper_xml_nms = Dir.entries(File.join(File.dirname(__FILE__), *%W[.. src main resources mapper]))
                    .reject { |entry| entry.match?(/^\..*$/) }
                    .map { |mapper_xml| mapper_xml.split(".").first }

mapper_xml_nms.each {|mapper_xml_nm|
  sqls = extract_and_substitute(
    File.join(
      File.dirname(__FILE__),
      %W[.. src main resources mapper #{mapper_xml_nm}.xml]
    )
  )

  sqls.each_with_index do |sql, idx|
    file_to_check = "#{mapper_xml_nm}_#{idx + 1}.sql"
    File.write("./hooks/.sql_check/#{file_to_check}", sql)
    puts `sqlfluff lint ./hooks/.sql_check/#{file_to_check} --dialect mysql | grep PRS`
  end
}
