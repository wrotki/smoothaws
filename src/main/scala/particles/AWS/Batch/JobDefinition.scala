package particles.AWS.Batch

import aws.Invoker.{cli, stgfy}
import com.definitelyscala.awssdk.Batch.{JobDefinitionContainerProperties, RegisterJobDefinitionParam}

import scala.scalajs.js
import js.JSConverters._

case class ContainerProperties(
                                image: String,
                                command: Seq[String],
                                memory: Long = 1024,
                                vcpus: Double = 1.0
                              )

case class JobDefinition(
                          jobDefinitionName: String,
                          containerProperties: ContainerProperties,
                          `type`: String = "container"
                        ) {
  def cliParams: RegisterJobDefinitionParam = {
    val containerProperties = js.Dynamic.literal(
      command = this.containerProperties.command.toJSArray,
      image = this.containerProperties.image,
      memory = this.containerProperties.memory,
      vcpus = this.containerProperties.vcpus
    ).asInstanceOf[JobDefinitionContainerProperties]
    js.Dynamic.literal(
      `type` = this.`type`,
      jobDefinitionName = this.jobDefinitionName,
      containerProperties = containerProperties
    ).asInstanceOf[RegisterJobDefinitionParam]
  }

  def make: (Either[Throwable, String] => Unit) => Unit = {
    cb => {
      val next: js.Function2[js.Any, js.Any, Unit] = { (x: js.Any, y: js.Any) =>
        val strg = stgfy(y)
        val error = stgfy(x)
        println("Result: " + strg)
        println("Error: " + x)
        x match {
          case null => cb(Right(strg))
          case e => cb(Left(new Throwable(error)))
        }
      }
      cli.registerJobDefinition(cliParams, next)
    }
  }
}