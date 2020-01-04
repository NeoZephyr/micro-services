## 账户数据模型
account
```
id
email
name
mobile
active
password_hash
avatar_url
support
```

createAccount
getOrCreateAccount
get
getAccountByMobile
list
updatePassword
verifyPassword
requestPasswordReset
update
syncUser
trackEvent

## 公司数据模型
company
```
id
name
archived
default_timezone
default_day_week_starts
```

## 管理员
admin
```
id
company_id
user_id
```

## 团队
team
```
id
company_id
name
archived
timezone
day_week_starts
color
```

## 目录
directory
```
id
company_id
user_id
internal_id
```

## 工人
worker
```
id
team_id
user_id
```

## 任务
```
id
team_id
name
archived
color
```

## shift
```
id
team_id
job_id
user_id
published
start
stop
```


createCompany
listCompanies
getCompany
updateCompany

createAdmin
listAdmins
getAdmin
getAdminOf
deleteAdmin

createDirectory
listDirectories
getDirectoryEntry
getAssociations
updateDirectoryEntry

createTeam
listTeams
getTeam
updateTeam
getWorkerTeamInfo


