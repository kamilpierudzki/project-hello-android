package com.project.hello.transit.agency.framework.internal.model.api

import com.project.hello.transit.agency.framework.R

enum class SupportedTransitStation(val file: Int) {
    POZNAN(R.raw.mpk_poznan_stops),
    KRAKOW(R.raw.mpk_krakow_stops),
    WARSZAWA(R.raw.wtp_warszawa_stops),
    WROCLAW(R.raw.mpk_wroclaw_stops),
    LODZ(R.raw.mpk_lodz_stops),
    GDANSK(R.raw.ztm_gdansk_stops),
    SZCZECIN(R.raw.zditm_szczecin_stops),
    BYDGOSZCZ(R.raw.zdmikp_bydgoszcz_stops),
    LUBLIN(R.raw.ztm_lublin_stops),
    BIALYSTOK(R.raw.mpk_bialystok_stops),
    SLUPSK(R.raw.zim_slupsk_stops),
    CZESTOCHOWA(R.raw.czestochowa_stops),
    OLSZTYN(R.raw.zdzit_olsztyn_stops),
    GORNOSLASKO_ZAGLEBOWSKA_METROPOLIA(R.raw.gzm_stops),
    KOLOBRZEG(R.raw.km_kolobrzeg_stops),
}