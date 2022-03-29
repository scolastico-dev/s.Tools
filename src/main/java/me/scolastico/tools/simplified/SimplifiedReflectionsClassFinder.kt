package me.scolastico.tools.simplified

import org.reflections.Reflections

/**
 * Find all classes in a package programmatically.
 */
class SimplifiedReflectionsClassFinder private constructor() {
    companion object {
        /**
         * Find all classes in a package programmatically. Uses java reflections.
         * @param packagePath The path to the package. Example: "me.scolastico.tools"
         * @return An array of all classes in the package.
         */
        fun getAllClassesInPackage(packagePath: String):Array<Class<*>> {
            val reflections = Reflections(packagePath)
            val allClasses = reflections.getSubTypesOf(Any::class.java)
            val ret = ArrayList<Class<*>>()
            for (c in allClasses) {
                ret.add(c)
            }
            return ret.toArray(emptyArray());
        }
    }
}
