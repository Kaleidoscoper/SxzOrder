# Instructions

Git global setup
```config
git config --global user.name "Evan Kwan"
git config --global user.email "ergouge@gmail.com"
```
Create a new repository
```config
git clone git@gitlab.com:ergouge/walker.git
cd walker
touch README.md
git add README.md
git commit -m "add README"
git push -u origin master
```
Existing folder or Git repository
```config
cd existing_folder
git init
git remote add origin git@gitlab.com:ergouge/walker.git
git add .
git commit
git push -u origin master
```

### 其他约定
clone代码到本地后，需切换到dev分支进行开发，之后的开发都基于此分支，除非一些feature、hotfix等需临时开辟分支