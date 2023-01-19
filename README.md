# CDP Case management plugin


## Development

Before running make sure to have the following *environment variables* defined correctly:

- AWS_ACCESS_KEY_ID
- AWS_SECRET_ACCESS_KEY
- S3_BUCKET_NAME
- S3_BUCKET_URL

*NOTE:* it's possible to avoid defining those variables by editing the configuration file `application.yml` and
defining the appropriate defaults in the following section:

```yaml
aws:
  s3:
    key: ${AWS_ACCESS_KEY_ID:my_access_key}
    secret: ${AWS_SECRET_ACCESS_KEY:my_secret_access_key}
    region: ${AWS_DEFAULT_REGION:eu-west-1}
    bucket:
      name: ${S3_BUCKET_NAME:entando-cdp-poc}
      url: ${S3_BUCKET_URL:fancy_aws_url}
```

to something similar to the following:  

```yaml
aws:
  s3:
    key: ${AWS_ACCESS_KEY_ID:AAABBCCCDDD}      # AAABBCCCDDD will be overridden if the AWS_ACCESS_KEY_ID is defined
    secret: ${AWS_SECRET_ACCESS_KEY:1234567890}   
    region: eu-west-1                          # this won't be overridden    
    bucket:
      name: ${S3_BUCKET_NAME:bucketName}
      url: ${S3_BUCKET_URL:https://bucket.url}
```

*Warning!* Be sure to not push your `application.yml` credentials! 

At this point it's possible to run the microservice from the root of the bundle by typing the command `ent bundle run case-service`

## Production