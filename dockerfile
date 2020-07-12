FROM node:latest as base
WORKDIR /app
COPY index.js ./
RUN npm install express

FROM astefanutti/scratch-node:10.16.0
WORKDIR /app
COPY --from=base /app/node_modules ./node_modules
COPY --from=base /app/index.js ./
ENTRYPOINT ["node"]

