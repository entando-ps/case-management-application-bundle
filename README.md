# CDP Case Management bundle 


## Development environment

Before running make sure to have the following *environment variables* defined correctly:

- AWS_ACCESS_KEY_ID
- AWS_SECRET_ACCESS_KEY
- S3_BUCKET_NAME
- AWS_DEFAULT_REGION (optional, the default `EU-WEST-1` will be used if not defined)

*NOTE:* it's possible to avoid defining those variables by editing the configuration file `application.yml` and
defining the appropriate defaults in the following section:

```yaml
aws:
  s3:
    key: ${AWS_ACCESS_KEY_ID:my_access_key}
    secret: ${AWS_SECRET_ACCESS_KEY:my_secret_access_key}
    region: ${AWS_DEFAULT_REGION:eu-west-1}
    bucket:
      name: ${S3_BUCKET_NAME:my_development_bucket}
```

to something similar to the following:  

```yaml
aws:
  s3:
    key: ${AWS_ACCESS_KEY_ID:AAABBCCCDDD}      # AAABBCCCDDD will be overridden if the AWS_ACCESS_KEY_ID is defined
    secret: ${AWS_SECRET_ACCESS_KEY:1234567890}   
    region: eu-west-1                          # this won't be overridden    
    bucket:
      name: ${S3_BUCKET_NAME:production-bucket-name}
```

*Warning!* Be sure to not push your `application.yml` credentials! 

At this point it's possible to run the microservice from the root of the bundle by typing the command `ent bundle run case-service`

## Production environment

Prepare a secret containing AWS_ACCESS_KEY_ID, AWS_SECRET_ACCESS_KEY and S3_BUCKET_NAME

This is done with the command:

```shell
ent k create secret generic e8827cec-case-management-bundle-secret --from-literal=keyId=<AWS_ACCESS_KEY_ID> --from-literal=secret=<AWS_SECRET_ACCESS_KEY> \
--from-literal=bucketname=<BUCKET_NAME> -n <NAMESPACE>
```

*NOTE:* the name of the secret is tailored after the **bundle id** (which changes with the repository) and the **bundle name** (declared in the `entando.json`) 
