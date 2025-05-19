require_relative './hook_report'

class BranchNameChecker
  def check(now_branch_nm)
    branch_nm_convention = /^(issue-[1-9][0-9]*|refactor)\/\w{1}(\w|-)*/

    if now_branch_nm.match(branch_nm_convention).nil?
      HookReport.new(
        'FAIL',
        ['branch name does not follow the convention!',
         'it should match either "issue-80/blah-blah" or "refactor/blah-blah"']
      )
    else
      HookReport.new(
        'SUCCESS',
        []
      )
    end
  end
end
