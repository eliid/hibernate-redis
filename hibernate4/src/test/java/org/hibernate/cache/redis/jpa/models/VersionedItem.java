package org.hibernate.cache.redis.jpa.models;

import com.google.common.base.Objects;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.cache.redis.AbstractHibernateEntity;

import javax.persistence.*;

/**
 * @author sunghyouk.bae@gmail.com
 */
@Entity
@org.hibernate.annotations.Cache(region = "common", usage = CacheConcurrencyStrategy.READ_WRITE)
@Getter
@Setter
public class VersionedItem extends AbstractHibernateEntity {


  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Version
  private Long version;

  private String name;
  private String description;

  @Override
  public int hashCode() {
    return Objects.hashCode(name, version);
  }

  private static final long serialVersionUID = -8467548718319032093L;
}
