/*
 * Copyright 2002-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.hibernate.cache.redis.hibernate5;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.boot.spi.SessionFactoryOptions;
import org.hibernate.cache.CacheException;
import org.hibernate.cache.redis.client.RedisClient;
import org.hibernate.cache.redis.hibernate5.regions.*;
import org.hibernate.cache.redis.hibernate5.strategy.RedisAccessStrategyFactory;
import org.hibernate.cache.redis.hibernate5.strategy.RedisAccessStrategyFactoryImpl;
import org.hibernate.cache.spi.*;
import org.hibernate.cache.spi.access.AccessType;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

/**
 * Abstract Hibernate 5.x 2nd Redis Region Factory
 *
 * @author sunghyouk.bae@gmail.com
 * @since 2015. 8. 27.
 */
@Slf4j
public abstract class AbstractRedisRegionFactory implements RegionFactory {

  protected final Properties props;
  protected SessionFactoryOptions options;
  protected final RedisAccessStrategyFactory accessStrategyFactory = new RedisAccessStrategyFactoryImpl();
  /**
   * Region names
   */
  protected final Set<String> regionNames = new HashSet<String>();

  /**
   * {@link RedisClient} instance.
   */
  protected volatile RedisClient redis = null;

  protected AbstractRedisRegionFactory(@NonNull Properties props) {
    this.props = props;
  }

  public boolean isMinimalPutsEnabledByDefault() {
    return true;
  }

  @Override
  public AccessType getDefaultAccessType() {
    return AccessType.READ_WRITE;
  }

  public long nextTimestamp() {
    return System.currentTimeMillis();
    //return Timestamper.next();
  }

  @Override
  public EntityRegion buildEntityRegion(String regionName,
                                        Properties properties,
                                        CacheDataDescription metadata) throws CacheException {
    regionNames.add(regionName);
    return new RedisEntityRegion(accessStrategyFactory,
                                 redis,
                                 regionName,
                                 options,
                                 metadata,
                                 properties);
  }

  @Override
  public NaturalIdRegion buildNaturalIdRegion(String regionName,
                                              Properties properties,
                                              CacheDataDescription metadata) throws CacheException {
    regionNames.add(regionName);
    return new RedisNaturalIdRegion(accessStrategyFactory,
                                    redis,
                                    regionName,
                                    options,
                                    metadata,
                                    properties);
  }

  @Override
  public CollectionRegion buildCollectionRegion(String regionName,
                                                Properties properties,
                                                CacheDataDescription metadata) throws CacheException {
    regionNames.add(regionName);
    return new RedisCollectionRegion(accessStrategyFactory,
                                     redis,
                                     regionName,
                                     options,
                                     metadata,
                                     properties);
  }

  @Override
  public QueryResultsRegion buildQueryResultsRegion(String regionName,
                                                    Properties properties) throws CacheException {
    regionNames.add(regionName);
    return new RedisQueryResultsRegion(accessStrategyFactory,
                                       redis,
                                       regionName,
                                       properties);
  }

  @Override
  public TimestampsRegion buildTimestampsRegion(String regionName,
                                                Properties properties) throws CacheException {
    // regionNames.add(regionName);
    return new RedisTimestampsRegion(accessStrategyFactory,
                                     redis,
                                     regionName,
                                     properties);
  }

  private static final long serialVersionUID = 4244155609146774509L;
}
