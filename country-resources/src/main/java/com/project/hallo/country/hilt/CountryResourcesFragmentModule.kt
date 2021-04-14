package com.project.hallo.country.hilt

import androidx.fragment.app.Fragment
import com.project.hallo.country.api.ResourceCountryCharacters
import com.project.hallo.country.internal.ResourceCountryCharactersImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.scopes.FragmentScoped

@Module
@InstallIn(FragmentComponent::class)
class CountryResourcesFragmentModule {

    companion object {

        @Provides
        @FragmentScoped
        fun bindResourceCountryCharacters(fragment: Fragment): ResourceCountryCharacters =
            ResourceCountryCharactersImpl(fragment.resources)
    }
}