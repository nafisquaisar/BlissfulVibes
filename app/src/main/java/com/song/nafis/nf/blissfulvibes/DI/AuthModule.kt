package com.song.nafis.nf.blissfulvibes.DI

import android.content.Context
import com.song.nafis.nf.blissfulvibes.Auth.AuthRepository
import com.song.nafis.nf.blissfulvibes.Auth.FirebaseAuthRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideAuthRepository(
        firebaseAuth: FirebaseAuth,
        @ApplicationContext context: Context
    ): AuthRepository {
        return FirebaseAuthRepository(firebaseAuth, context)
    }
}
