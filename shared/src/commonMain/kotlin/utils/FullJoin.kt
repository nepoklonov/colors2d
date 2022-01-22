package utils

private fun <F, S> fullJoinWithIterator(iterator: Iterator<F>, other: Iterable<S>): Sequence<Pair<F, S>> {
    var otherIterator = other.iterator()
    if (!otherIterator.hasNext()) return emptySequence()
    var current = iterator.next()
    return generateSequence {
        if (!otherIterator.hasNext()) {
            otherIterator = other.iterator()
            if (iterator.hasNext()) {
                current = iterator.next()
            } else return@generateSequence null
        }
        current to otherIterator.next()
    }
}

infix fun <F, S> Iterable<F>.fullJoin(other: Iterable<S>): Sequence<Pair<F, S>> {
    return fullJoinWithIterator(iterator(), other)
}

infix fun <F, S> Sequence<F>.fullJoin(other: Iterable<S>): Sequence<Pair<F, S>> {
    return fullJoinWithIterator(iterator(), other)
}

fun <F, S, T> Iterable<F>.fullJoin(other: Iterable<S>, transform: (F, S) -> T): Sequence<T> {
    val joinResult = this fullJoin other
    return joinResult.map { (f, s) -> transform(f, s) }
}

fun <F, S, T> Sequence<F>.fullJoin(other: Iterable<S>, transform: (F, S) -> T): Sequence<T> {
    val joinResult = this fullJoin other
    return joinResult.map { (f, s) -> transform(f, s) }
}