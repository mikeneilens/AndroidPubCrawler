package michaelneilens.pubCrawler.Extensions

fun <T> List<T>.swap(index1: Int, index2: Int):List<T> {
    var tempList = this.toMutableList()

    val tempElement = tempList[index1] // 'this' corresponds to the list
    tempList[index1] = this[index2]
    tempList[index2] = tempElement

    return tempList.toList()
}

fun List<String>.asArray():Array<String> {
    var arrayList:ArrayList<String> = ArrayList(this)
    val array = arrayOfNulls<String>(arrayList.size)
    return  arrayList.toArray(array)
}