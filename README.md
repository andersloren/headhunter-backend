# Welcome to the Headhunter project

This is the backend part of the internship project Headhunter created by [Anders Lorén](https://github.com/andersloren) and [Mikael Engvall](https://github.com/MikaelEngvall).

The frontend can be found (here)[https://github.com/andersloren/headhunter-frontend).

## MySQL Scripts

#### Entity Tables
`SELECT * FROM jobs; SELECT * FROM _users; SELECT * FROM ads;`

#### User - Job Relationship table
`-- Relationship Table between USERS and JOBS SELECT u.email AS user_email, u.username AS user_username, j.id AS job_id, j.title AS job_title FROM _users u JOIN jobs j ON u.email = j.user_id ORDER BY u.email, j.id;`

#### Job - Ad Relationship table
`-- Relationship Table between JOBS and ADS SELECT j.id AS job_id, j.title AS job_title, a.id AS ad_id, a.html_code AS ad_htmlCode FROM jobs j JOIN ads a ON j.id = a.job_id ORDER BY j.id, a.id;`
