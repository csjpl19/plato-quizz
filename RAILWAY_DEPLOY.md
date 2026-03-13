# Deploiement sur Railway

## Fichiers utilises
- `railway.json` (build/deploy Railway)
- `Dockerfile` (build et run de l'app)
- `src/main/resources/application-prod.properties` (compatible Railway + Render)

## Etapes
1. Cree un nouveau projet Railway depuis ton repo GitHub.
2. Ajoute un service PostgreSQL dans le meme projet.
3. Dans le service applicatif, configure ces variables:
   - `SPRING_PROFILES_ACTIVE=prod`
   - `SESSION_COOKIE_SECURE=true`
   - `GOOGLE_CLIENT_ID=<ton_client_id>`
   - `GOOGLE_CLIENT_SECRET=<ton_client_secret>`

## Base de donnees (recommande)
Definis ces variables dans le service applicatif avec des references Railway:
- `SPRING_DATASOURCE_URL=jdbc:postgresql://${{Postgres.PGHOST}}:${{Postgres.PGPORT}}/${{Postgres.PGDATABASE}}`
- `SPRING_DATASOURCE_USERNAME=${{Postgres.PGUSER}}`
- `SPRING_DATASOURCE_PASSWORD=${{Postgres.PGPASSWORD}}`

Si ton service PostgreSQL n'est pas nomme `Postgres`, remplace `Postgres` par le nom reel.

## Healthcheck
Le healthcheck est configure sur `/login` dans `railway.json`.
