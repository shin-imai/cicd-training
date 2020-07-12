FROM node:latest as base
WORKDIR /app
RUN npm install express

FROM astefanutti/scratch-node:10.16.0
COPY --from=base /app/node_modules ./app/node_modules
COPY --from=base /app/package-lock.json ./app/
ENTRYPOINT node

