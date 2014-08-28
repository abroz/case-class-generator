//TODO Unit is not yet supported as a field member's type

import com.julianpeeters.caseclass.generator._
import com.novus.salat._
import com.novus.salat.global._
import com.mongodb.casbah.Imports._



import org.specs2._
import mutable._
import specification._

class UnitUnitSpec extends mutable.Specification {


  val valueMembers: List[FieldSeed] = List(FieldSeed("a","Unit"), FieldSeed("b","Unit"))
  val classData = ClassData(Some("models"), "MyRecord_UnitUnitSpec", valueMembers)
  val dcc = new DynamicCaseClass(classData)


  val typeTemplate = dcc.runtimeInstance

  type MyRecord = typeTemplate.type



  val dbo = grater[MyRecord].asDBObject(typeTemplate)
    //println(dbo)

  val obj = grater[MyRecord].asObject(dbo)
    println(obj)
 
 "given a dynamically generated case class MyRecord_UnitUnitSpec(a: Unit, b: Unit) as a type parameter, a grater" should {
    "serialize and deserialize correctly" in {
      typeTemplate === obj
    }
}



}
