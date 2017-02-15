package org.scala.examples

/**
 * Created by pengcheng.wan on 2017/2/15.
 */

import scala.io.Source
import scala.collection.mutable.HashMap


/**
  * 实现Hadoop中WordCount功能
  * @author zhangyun
  */
object WordCount{
  def main(args: Array[String]): Unit = {

    val source = Source.fromFile("input/SSH免密码.txt").mkString
    val tokens = source.split("\\s+")
    val mutablemap = new HashMap[String, Int]
    for (key <-tokens)
    {
      mutablemap(key) = mutablemap.getOrElse(key, 0) + 1
    }
    println(mutablemap.mkString("  "))


  }
}