FROM clojure:tools-deps-1.11.1.1208-alpine AS build

WORKDIR /build
COPY . .
RUN apk update && apk add --update nodejs npm
RUN npm install
RUN clojure -A:dev -m shadow.cljs.devtools.cli compile app
RUN clojure -A:dev -T:build uber

RUN ls
RUN ls target

FROM openjdk:19-slim

WORKDIR /app
ARG WEB_RESOURCES_PATH="resources/public"
ARG ASSETS_PATH="resources/assets"

COPY --from=build /build/target/imup.jar imup.jar
COPY --from=build /build/resources/public $WEB_RESOURCES_PATH
COPY --from=build /build/resources/assets $ASSETS_PATH

RUN ls

EXPOSE 8080

CMD ["java", "-jar", "imup.jar", "prod"]