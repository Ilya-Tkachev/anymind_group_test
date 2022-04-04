package com.example.demo.validation

interface CheckSingle<T> {
    val message: String
    val check: (value: T) -> Boolean
}

interface CheckTuple<T, U> {
    val message: String
    val check: (firstValue: T, secondValue: U) -> Boolean
}