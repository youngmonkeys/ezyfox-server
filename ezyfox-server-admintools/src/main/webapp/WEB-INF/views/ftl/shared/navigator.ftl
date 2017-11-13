<#assign accessToken>
	<@s.property name="accessToken" value="%{#session.accessToken}" />
</#assign>

<@s.url action="logout" var="logout" page="logout" accessToken=accessToken />
<@s.url action="navigate" var="home" page="home" />

<@s.url action="add_admin" var="add_admin" accessToken=accessToken first="true" />
<@s.url action="list_admin" var="list_admin" accessToken=accessToken first="true" />
<@s.url action="update_admin" var="update_admin" accessToken=accessToken  first="true" />
<@s.url action="create_group" var="create_group" accessToken=accessToken first="true" />
<@s.url action="grant_permission" var="grant_permission" accessToken=accessToken  first="true" />

<@s.url action="config_room" var="config_room" accessToken=accessToken first="true" />
<@s.url action="config_jackpot" var="config_jackpot" accessToken=accessToken first="true" />
<@s.url action="jackpot_boot" var="jackpot_boot" accessToken=accessToken first="true" />
<@s.url action="pay_precious_game" var="pay_precious_game" accessToken=accessToken first="true" />
<@s.url action="config_golden_time" var="config_golden_time" accessToken=accessToken first="true" />
<@s.url action="limit_jackpot_win" var="limit_jackpot_win" accessToken=accessToken first="true" />
<@s.url action="config_bigwin_mode" var="config_bigwin_mode" accessToken=accessToken first="true" />

<@s.url action="report_revenue" var="report_revenue" accessToken=accessToken first="true" />
<@s.url action="report_player" var="report_player" accessToken=accessToken first="true" />
<@s.url action="report_jackpot" var="report_jackpot" accessToken=accessToken first="true" />
<@s.url action="report_jackpot_winning" var="report_jackpot_winning" accessToken=accessToken first="true" />
<@s.url action="report_bonus_game" var="report_bonus_game" accessToken=accessToken first="true" />

<@s.url action="list_players" var="list_players" accessToken=accessToken first="true" />
<@s.url action="history_player" var="history_player" accessToken=accessToken first="true" />
<@s.url action="history_winning" var="history_winning" accessToken=accessToken first="true" />

<@s.url action="list_win_jackpot" var="list_win_jackpot" accessToken=accessToken first="true" />
<@s.url action="list_jackpot" var="list_jackpot" accessToken=accessToken first="true" />
<@s.url action="list_winning" var="list_winning" accessToken=accessToken first="true" />
<@s.url action="list_bonus_error" var="list_bonus_error" accessToken=accessToken first="true" />

<@s.url action="account_info" var="account_info" accessToken=accessToken first="true" />
<@s.url action="account_update" var="account_update" accessToken=accessToken first="true" />
<@s.url action="reset_password" var="reset_password" accessToken=accessToken first="true" />

<@s.url action="game_overview" var="game_overview" accessToken=accessToken first="true" />
<@s.url action="game_accounts" var="game_accounts" accessToken=accessToken first="true" />
<@s.url action="game_account_detail" var="game_account_detail" accessToken=accessToken first="true" />
<@s.url action="game_rooms" var="game_rooms" accessToken=accessToken first="true" />
<@s.url action="game_jackpot" var="game_jackpot" accessToken=accessToken first="true" />
<@s.url action="game_jackpot_detail" var="game_jackpot_detail" accessToken=accessToken first="true" />
<@s.url action="game_transaction" var="game_transaction" accessToken=accessToken first="true" />
<@s.url action="game_transaction_detail" var="game_transaction_detail" accessToken=accessToken first="true" />
<@s.url action="export_data_user" var="export_data_user" accessToken=accessToken first="true" />
<@s.url action="game_system_log" var="game_system_log" accessToken=accessToken first="true" />
<@s.url action="game_system_bug" var="game_system_bug" accessToken=accessToken first="true" />
<@s.url action="game_system_log_detail" var="game_system_log_detail" accessToken=accessToken first="true" />
<@s.url action="jackpot_boot_history" var="jackpot_boot_history" accessToken=accessToken first="true" />


<@s.url action="list_events" var="list_events" accessToken=accessToken first="true" />
<@s.url action="event_detail" var="event_detail" accessToken=accessToken first="true" />
<@s.url action="add_event" var="add_event" accessToken=accessToken first="true" />

<@s.url action="paytable_get" var="paytable_get" accessToken=accessToken/>
