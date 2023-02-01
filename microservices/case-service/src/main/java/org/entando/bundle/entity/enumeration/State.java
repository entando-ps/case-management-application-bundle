package org.entando.bundle.entity.enumeration;

public enum State {
  CREATED,
  RUNNING,
  COMPLETED,
  DELETED // but at least one resource could not be removed from S3
}
