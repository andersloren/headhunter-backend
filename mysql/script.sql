/**
 This script can be used in MySQL (and maybe other SQL:s) to monitor the relationship tables.
 */

SELECT * FROM job;
SELECT * FROM account;
SELECT * FROM ad;
SELECT * FROM account_info;

-- Relationship Table between ACCOUNT and JOB
SELECT
    ac.email AS account_email,
    ac.username AS account_username,
    j.id AS job_id,
    j.title AS job_title
FROM
    account u
JOIN
    job j ON u.email = j.account_id
ORDER BY
    u.email, j.id;

-- Relationship Table between JOB and AD
SELECT
    j.id AS job_id,
    j.title AS job_title,
    a.id AS ad_id,
    a.html_code AS ad_htmlCode
FROM
    job j
JOIN
    a ad ON j.id = a.job_id
ORDER BY
    j.id, a.id;
