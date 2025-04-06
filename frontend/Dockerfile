# syntax=docker/dockerfile:1
# escape=`


# Stage 1: Build
FROM node:22 as build
WORKDIR /app
COPY package.json ./
RUN npm install
COPY . .
RUN npm run build


# Stage 2: Production Stage
FROM nginx:alpine
COPY --from=build app/dist /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
