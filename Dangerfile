#case mrName=gitlab.mr_title.downcase:
#when mrName.start_with? "feature/"
#  
#when mrName.start_with? "rc"
#when mrName.start_with? "hotfix"
#
#when mrName.start_with? "support"
#
#if !(gitlab.mr_title.downcase.include? "feature/")
#   warn("")
#end
#
#def checkForLabels()
#  fail("Please provide relevant team label the MR belongs to") if gitlab.mr_labels.empty?
#end
#
def checkForMergeCommits()
  if git.commits.any? { |c| c.message =~ /^Merge branch/ }
  warn 'Please rebase to get rid of the merge commits in this PR'
  end
end

#def checkForGradleModifications()
#  if(git.modified_files.)




