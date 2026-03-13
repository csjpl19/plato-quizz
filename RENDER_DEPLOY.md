# Deploiement sur Render

## Fichiers prepares
- `render.yaml` : Blueprint Render (web service + base PostgreSQL managée)
- `Dockerfile` : build et run de l'application Spring Boot
- `src/main/resources/application-prod.properties` : config production (PostgreSQL)
- `src/main/resources/application-local.properties` : config locale (Oracle)

## Variables Render a renseigner
Dans Render, lors de l'import du Blueprint:
- `GOOGLE_CLIENT_ID` (secret)
- `GOOGLE_CLIENT_SECRET` (secret)

Les variables de base de donnees sont injectees automatiquement via `fromDatabase`.

## Commandes locales utiles
```powershell
# build local (sans tests)
.\mvnw.cmd -DskipTests clean package
```

## Lancement Blueprint
1. Commit/push les changements sur GitHub/GitLab/Bitbucket.
2. Ouvre ce lien dans ton navigateur en remplaçant l'URL du repo:
   `https://dashboard.render.com/blueprint/new?repo=https://github.com/<user>/<repo>`
3. Renseigne les secrets Google.
4. Clique `Apply`.
