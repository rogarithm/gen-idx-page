#!/usr/bin/env ruby

now_branch_nm = `git rev-parse --abbrev-ref HEAD`
branch_nm_convention = /^issue\/[1-9][0-9]*(\w|-)+/

if now_branch_nm.match(branch_nm_convention).nil?
  puts 'branch name does not follow the convention!'
  puts 'it should match "issue/80-blah-blah"'
  exit 1
end
