package com.julianpeeters.caseclass.generator

import org.objectweb.asm._
import Opcodes._

case class Init(cw: ClassWriter, var mv: MethodVisitor, caseClassName: String, fieldData: List[EnrichedField], ctorReturnType: String) {
  def dump = {

    //init method
    if (fieldData.map(fd => fd.fieldType).exists(ft => ft.takesTypeArgs)) {
      mv = cw.visitMethod(ACC_PUBLIC, "<init>", ctorReturnType, "(" + fieldData.map(fd => fd.typeData.unerasedTypeDescriptor).mkString + ")V", null);
    }
    else mv = cw.visitMethod(ACC_PUBLIC, "<init>", ctorReturnType, null, null);
    mv.visitCode();

    var stackIndex = 1

    fieldData.map(fd => { 
      if (fd.typeData.loadInstr == DLOAD | fd.typeData.loadInstr == LLOAD) {
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(fd.typeData.loadInstr, stackIndex);
        mv.visitFieldInsn(PUTFIELD, caseClassName, fd.fieldName, fd.typeData.typeDescriptor.toString);
        stackIndex += 2
      }
      else {
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(fd.typeData.loadInstr, stackIndex);
        mv.visitFieldInsn(PUTFIELD, caseClassName, fd.fieldName, fd.typeData.typeDescriptor.toString);
        stackIndex += 1;
      }
    })

    mv.visitVarInsn(ALOAD, 0);
    mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V");
    mv.visitVarInsn(ALOAD, 0);
    mv.visitMethodInsn(INVOKESTATIC, "scala/Product$class", "$init$", "(Lscala/Product;)V");
    mv.visitInsn(RETURN);
    mv.visitMaxs(2, 4);
    mv.visitEnd();

    cw.visitEnd();
  }
}
