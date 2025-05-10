#!/usr/bin/env ruby

require_relative './lib/branch_name_checker'

report = BranchNameChecker.new.check(ARGV[0])
if report.status == 'FAIL'
  puts report.msgs.join("\n")
  exit 1
end
