package com.project.hello.vehicle.prediction.steps.implementation

import com.project.hello.vehicle.prediction.steps.Fragmentation

class FragmentationImpl : Fragmentation {

    override fun fragmentedInput(input: String): List<String> {
        val fromBeginning = input
            .mapIndexed { index, _ ->
                input.substring(IntRange(0, index))
            }

        val fromEnd = input
            .mapIndexed { index, _ ->
                input.substring(IntRange(input.length - index - 1, input.length - 1))
            }

        return fromBeginning + fromEnd
    }
}