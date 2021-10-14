package com.project.hello.transit.agency.framework.internal.model.api

import com.project.hello.transit.agency.framework.R

internal enum class SupportedTransitAgency(val file: Int) {
    POZNAN(R.raw.mpk_poznan),
    KRAKOW(R.raw.mpk_krakow),
    WARSZAWA(R.raw.wtp_warszawa),
    WROCLAW(R.raw.mpk_wroclaw),
    LODZ(R.raw.mpk_lodz),
    GDANSK(R.raw.ztm_gdansk),
    SZCZECIN(R.raw.zditm_szczecin),
    BYDGOSZCZ(R.raw.zdmikp_bydgoszcz),
    LUBLIN(R.raw.ztm_lublin),
    BIALYSTOK(R.raw.mpk_bialystok),
    SLUPSK(R.raw.zim_slupsk),
    CZESTOCHOWA(R.raw.czestochowa),
    OLSZTYN(R.raw.zdzit_olsztyn),
    GORNOSLASKO_ZAGLEBOWSKA_METROPOLIA(R.raw.gzm_ztm),
    KOLOBRZEG(R.raw.km_kolobrzeg),
    MPK_SWIDNICA(R.raw.mpk_swidnica),
    BIELSKO_BIALA(R.raw.mzk_bielsko_biala),
    MZK_GORZOW_WLKP(R.raw.mzk_gorzow_wielkopolski),
    ZTM_KIELCE(R.raw.ztm_kielce),
}