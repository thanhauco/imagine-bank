$env:GIT_AUTHOR_NAME="Thanh Vu"
$env:GIT_AUTHOR_EMAIL="thanhauco@gmail.com"
$env:GIT_COMMITTER_NAME="Thanh Vu"
$env:GIT_COMMITTER_EMAIL="thanhauco@gmail.com"

git add .
git commit -m "Feat: Implement Fraud Detection Engine (Service, Integration)"

# Run history rewrite
sh ./fix_author.sh

# Cleanup
rm ./fix_author.sh
git update-ref -d refs/original/refs/heads/master
