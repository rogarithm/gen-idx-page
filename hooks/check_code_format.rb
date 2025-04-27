#!/usr/bin/env ruby

checkstyle_result = `./gradlew checkstyleMain checkstyleTest 2>/dev/null`
is_checkstyle_failed = checkstyle_result.split("\n")
                                        .select {|l|
                                          l.match(/^> Task :checkstyleMain FAILED.*|^> Task :checkstyleTest FAILED.*/)
                                        } != []

editorconfig_result = `./gradlew editorconfigCheck 2>/dev/null`
is_editorconfig_failed = editorconfig_result.split("\n")
                                            .select {|l|
                                              l.match(/^> Task :editorconfigCheck FAILED.*/)
                                            } != []

if is_checkstyle_failed
  puts 'failed to format code as configured in checkstyle!'
  puts 'run ./gradlew check for detail'
end
if is_editorconfig_failed
  puts 'trying to recover editorconfig failure...'
  puts 'commit before push'
  puts 'run ./gradlew editorConfigFormat'
end

if is_checkstyle_failed or is_editorconfig_failed
  exit 1
end
