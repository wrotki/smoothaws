package particles.AWS

import cats.effect.IO

package object Batch {


  def registerJobDefIfMissing(jobName: String): IO[Unit] = {
    println("inside batchRegisterJobDefIfMissing")
    for {
      jobDefList <- JobDefinitions.list
      _ <- IO(System.err.println(s"List done"))
      brjd <- registerJobDef(jobName, jobDefList)
      last <- IO(System.err.println(s"batchRegisterJobDef: $brjd"))
    } yield last
  }

  private def registerJobDef(jobName: String, currentJobDefs: Seq[JobDefinition]): IO[String] = {
    println(s"inside batchRegisterJobDef, jobName: $jobName")
    println(s"JobDefs: $jobName")
    currentJobDefs filter {
      _.jobDefinitionName == jobName
    } map println

    if (currentJobDefs.exists(_.jobDefinitionName == jobName)) {
      IO(s"$jobName already there")
    } else {
      //      IO(s"$jobName not there, will create")
      println(s"$jobName not there, will create")
      IO.async {
        println("inside batchRegisterTwoJobDefs async")
        JobDefinition(jobName,
          jobName,
          0,
          "ACTIVE",
          "container",
          ContainerProperties(
            "VanKlomp",
            List("bash", "-c", "sleep 10"),
            1024,
            1.0
          )
        ).register
      }
    }
  }
}

