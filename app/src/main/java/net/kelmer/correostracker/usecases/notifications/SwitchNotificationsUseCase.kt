package net.kelmer.correostracker.usecases.notifications

import io.reactivex.Single
import net.kelmer.correostracker.base.usecase.rx.RxSingleUseCase
import net.kelmer.correostracker.data.repository.local.LocalParcelRepository
import javax.inject.Inject

class SwitchNotificationsUseCase @Inject constructor(private val localParcelRepository: LocalParcelRepository) : RxSingleUseCase<SwitchNotificationsUseCase.Params, String>() {

    data class Params(val parcelCode: String, val enable: Boolean)

    override fun buildUseCase(params: Params): Single<String> {
        return localParcelRepository.setNotify(params.parcelCode, params.enable).toSingleDefault(params.parcelCode)
    }
}
