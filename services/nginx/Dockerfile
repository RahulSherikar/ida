FROM node:10.15-stretch as builder

WORKDIR /app
COPY ida-chatbot/package.json package.json
COPY ida-chatbot/package-lock.json package-lock.json
RUN npm ci

COPY ida-chatbot/src src
COPY ida-chatbot/angular.json angular.json
COPY ida-chatbot/tsconfig.json tsconfig.json
COPY ida-chatbot/tslint.json tslint.json
RUN npm run prod-container

FROM nginx:1.15.8

EXPOSE 80

COPY services/nginx/mime.types /etc/nginx/mime.types
COPY services/nginx/nginx.conf /etc/nginx/nginx.conf

COPY --from=builder /app/dist/ida-chatbot /app
