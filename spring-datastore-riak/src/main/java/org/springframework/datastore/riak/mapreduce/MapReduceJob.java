/*
 * Copyright (c) 2010 by J. Brisbin <jon@jbrisbin.com>
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

package org.springframework.datastore.riak.mapreduce;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * @author J. Brisbin <jon@jbrisbin.com>
 */
public interface MapReduceJob<T> extends Callable {

  List<Object> getInputs();

  <V> MapReduceJob addInputs(List<V> keys);

  MapReduceJob addPhase(MapReducePhase phase);

  void setArg(T arg);

  <T> T getArg();

  String toJson();
}
