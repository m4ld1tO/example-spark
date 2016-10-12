// Copyright (C) 2011-2012 the original author or authors.
// See the LICENCE.txt file distributed with this work for additional
// information regarding copyright ownership.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.mkuthan.spark

import org.apache.spark.sql.SparkSession
import org.apache.spark.{SparkConf, SparkContext}
import org.scalatest.{BeforeAndAfterAll, Suite}

trait SparkSpec extends BeforeAndAfterAll {
  this: Suite =>

  private var _spark: SparkSession = _

  override def beforeAll(): Unit = {
    super.beforeAll()

    val conf = new SparkConf()
      .setMaster("local[*]")
      .setAppName(this.getClass.getSimpleName)

    sparkConfig.foreach { case (k, v) => conf.setIfMissing(k, v) }

    _spark = SparkSession.builder()
      .config(conf)
      .enableHiveSupport()
      .getOrCreate()
  }

  override def afterAll(): Unit = {
    if (_spark != null) {
      _spark.stop()
      _spark = null
    }
    super.afterAll()
  }

  def sparkConfig: Map[String, String] = Map.empty

  def spark: SparkSession = _spark

  def sc: SparkContext = _spark.sparkContext

}
