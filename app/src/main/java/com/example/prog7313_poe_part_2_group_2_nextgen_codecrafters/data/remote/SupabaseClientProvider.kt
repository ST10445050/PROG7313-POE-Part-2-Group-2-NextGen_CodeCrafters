package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.remote

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.postgrest.Postgrest

object SupabaseClientProvider {

    val client = createSupabaseClient(
        supabaseUrl = "https://snorqtjcgudljqovwhzi.supabase.co",
        supabaseKey = "sb_publishable_rdi2fpexJ0-gZxZi-QlJEQ_bsAgHkyE"
    ) {
        install(Auth)
        install(Postgrest)
    }
}