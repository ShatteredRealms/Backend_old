#!/bin/sh

# $JASYPT_ENCRYPT_PASSWORD should be set
if [ -z "$JASYPT_ENCRYPT_PASSWORD" ]; then
  echo "Jasypt encrypt password not set. Please set JASYPT_ENCRYPT_PASSWORD varaible" >&2
  exit 1
fi

# Start the app
java -jar /app.jar \
  --spring.profiles.active=prod \
  --jasypt.encryptor.algorithm=PBEWithMD5AndDES \
  --jasypt.encryptor.iv-generator-classname=org.jasypt.iv.NoIvGenerator \
  --jasypt.encryptor.password=$JASYPT_ENCRYPT_PASSWORD