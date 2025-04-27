#!/usr/bin/env ruby

now_branch_nm = ARGV[0]
branch_nm_convention = /^issue\/[1-9][0-9]*(\w|-)+/

puts "current branch name is #{now_branch_nm}"

if now_branch_nm.match(branch_nm_convention).nil?
  puts 'branch name does not follow the convention!'
  puts 'it should match "issue/80-blah-blah"'
  exit 1
end
