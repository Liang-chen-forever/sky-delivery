# Security Policy

Do not commit passwords, access keys, API tokens, private keys, certificates, local configuration files, or user data to this repository.

If a credential is accidentally exposed, revoke or rotate it immediately in the relevant provider console, then remove it from the working tree and Git history before publishing again. Report the incident privately to the repository owner rather than opening a public issue.

The example configuration in `sky-take-out/sky-server/src/main/resources/application-dev.example.yml` contains placeholders only. Local `application-dev.yml` and WeChat private project configuration are intentionally ignored.
