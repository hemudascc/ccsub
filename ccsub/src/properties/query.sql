 ALTER TABLE `cc_sub`.`tb_intarget_config` ADD COLUMN `operator_id` INT NULL AFTER `service_id`; 
 
 ALTER TABLE `cc_sub`.`tb_intarget_config` ADD COLUMN `unsub_keyword` VARCHAR(255) NULL AFTER `keyword`; 
  
 ALTER TABLE `cc_sub`.`tb_intarget_ussd_trans` ADD COLUMN `msg_type` VARCHAR(100) NULL AFTER `action`; 
 
 #######################
 
 ALTER TABLE `cc_sub`.`tb_intarget_config` ADD COLUMN `welcome_activation_message` VARCHAR(255) NULL AFTER `activation_message`, ADD COLUMN `deactivation_message` VARCHAR(255) NULL AFTER `welcome_activation_message`; 
 
 ALTER TABLE `cc_sub`.`tb_intarget_config` CHANGE `activation_message` `billing_activation_message` VARCHAR(255) CHARSET utf8 COLLATE utf8_general_ci NULL; 
 #################
 
 ALTER TABLE `cc_sub`.`tb_th_config` ADD COLUMN `operartor_name` VARCHAR(50) NULL AFTER `product_id`; 
 
 ############
 ALTER TABLE `cc_sub`.`tb_nxt_webhook_notification` ADD COLUMN `language` VARCHAR(10) NULL AFTER `transacton_id`; 
 ALTER TABLE `cc_sub`.`tb_nxt_webhook_notification` ADD COLUMN `subscriber_status` VARCHAR(20) NULL AFTER `subscriber_id`; 
 ##############################
 
 ALTER TABLE `cc_sub`.`tb_messagecloud_notification` ADD COLUMN `action` VARCHAR(50) NULL AFTER `id`, ADD COLUMN `query_str` TEXT NULL AFTER `network`; 
 ALTER TABLE `cc_sub`.`tb_messagecloud_notification` ADD COLUMN `send_to_adnetwork` TINYINT(1) DEFAULT 0 NULL AFTER `create_time`; 
 ##################
 ALTER TABLE `cc_sub`.`tb_oredoo_kuwait_service_config` ADD COLUMN `plan_id` VARCHAR(100) NULL AFTER `service_node`; 
 ALTER TABLE `cc_sub`.`tb_oredoo_kuwait_fallback_price_point` ADD COLUMN `plan_id` VARCHAR(50) NULL AFTER `service_id`; 
 #################
 
 ALTER TABLE `cc_sub`.`tb_veoo_delivery_receipt` ADD COLUMN `veoo_timestamp` VARCHAR(100) NULL AFTER `mt_id`, ADD COLUMN `veoo_service_id` VARCHAR(255) NULL AFTER `veoo_timestamp`; 
 
 ###########
 ALTER TABLE `cc_sub`.`adnetwork_tokens` ADD COLUMN `duplicate_click` TINYINT(1) DEFAULT 0 NULL AFTER `referer`; 
 
 ##########
 ALTER TABLE `cc_sub`.`tb_veoo_service_config` CHANGE `content_message_template` `billing_content_message_template` VARCHAR(255) CHARSET utf8 COLLATE utf8_general_ci NULL; 
 ########
 
  ALTER TABLE `cc_sub`.`tb_oredoo_kuwait_cg_callback` ADD COLUMN `used` TINYINT(1) DEFAULT 0 NULL AFTER `subscripton_api_response`;
  
  
  ###########
ALTER TABLE `cc_sub`.`tb_subscribers_reg` CHANGE `param1` `param1` TEXT CHARSET utf8 COLLATE utf8_general_ci NULL, DROP INDEX `key_param1`, DROP INDEX `subecrber_key`;
tb_veoo_click_flow_trans

  ##########
  
  ALTER TABLE `cc_sub`.`tb_bizao_config` ADD COLUMN `service_type` VARCHAR(50) NULL AFTER `msisdn_prefix`; 
  #############
  
  ALTER TABLE `cc_sub`.`tb_inapp_status_check` ADD COLUMN `action` VARCHAR(100) NULL AFTER `id`; 
  ALTER TABLE `cc_sub`.`tb_inapp_status_check` ADD COLUMN `retry_counter` INT NULL AFTER `create_time`; 
  
  ALTER TABLE `cc_sub`.`tb_inapp_status_check` CHANGE `sub_status` `sub_status` VARCHAR(100) NULL, ADD COLUMN `description` VARCHAR(100) NULL AFTER `sub_status`, ADD COLUMN `trx_id` VARCHAR(100) NULL AFTER `description`, ADD COLUMN `charge_status` VARCHAR(100) NULL AFTER `trx_id`, CHANGE `response_to_caller` `response_to_caller` VARCHAR(255) CHARSET utf8 COLLATE utf8_general_ci NULL AFTER `create_time`; 
  
  RENAME TABLE `cc_sub`.`tb_inapp_config` TO `cc_sub`.`tb_tmt_inapp_config`; 
  RENAME TABLE `cc_sub`.`tb_inapp_otp_send` TO `cc_sub`.`tb_tmt_inapp_otp_send`; 
  RENAME TABLE `cc_sub`.`tb_inapp_otp_validation` TO `cc_sub`.`tb_tmt_inapp_otp_validation`;
  RENAME TABLE `cc_sub`.`tb_inapp_status_check` TO `cc_sub`.`tb_tmt_inapp_status_check`; 
  ALTER TABLE `cc_sub`.`tb_tmt_inapp_otp_send` ADD COLUMN `request_id` BIGINT UNSIGNED NULL AFTER `cmpid`, ADD COLUMN `cg_token` VARCHAR(200) NULL AFTER `request_id`, CHANGE `transation_id` `trx_id` VARCHAR(255) CHARSET utf8 COLLATE utf8_general_ci NULL; 
  ###################
  
  ALTER TABLE `cc_sub`.`tb_aggregator` ADD COLUMN `type` VARCHAR(100) NULL AFTER `name`; 
  
  ##################
   CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `vw_live_report_monthly` AS select `tcc`.`adnetwork_campaign_id` AS `adnetwork_campaign_id`,`tcc`.`pub_id` AS `pub_id`,`tcc`.`type` AS `type`,`top`.`operator_name` AS `operator_name`,`top`.`aggregator_id` AS `aggregator_id`,`top`.`operator_id` AS `operator_id`,`vwac`.`network_name` AS `network_name`,`vwac`.`ad_network_id` AS `adnetworkid`,cast(`tcc`.`report_date` as date) AS `report_date`,`tcc`.`click_count` AS `click_count`,`tcc`.`valid_click_count` AS `valid_click_count`,`tcc`.`conversion_count` AS `conversion_count`,`tcc`.`send_conversion_count` AS `send_conversion_count`,`tcc`.`send_manual_conversion_count` AS `send_manual_conversion_count`,(`tcc`.`conversion_count` - `tcc`.`send_conversion_count`) AS `conversion_notsent`,`tcc`.`amount` AS `amount`,`tcc`.`duplicate_click_count` AS `duplicate_click_count`,`tcc`.`reverse_click_count` AS `reverse_click_count`,`tcc`.`block_click_count` AS `block_click_count`,`tcc`.`already_subscribed_count` AS `already_subscribed_count`,`tcc`.`duplicate_conversion_count` AS `duplicate_conversion_count`,`tcc`.`grace_conversion_count` AS `grace_conversion_count`,`tcc`.`grace_send_conversion_count` AS `grace_send_conversion_count`,`tcc`.`dct_count` AS `dct_count`,`tcc`.`dct_churn_count` AS `dct_churn_count`,`tcc`.`renewal_count` AS `renewal_count`,`tcc`.`renewal_sentcount` AS `renewal_sentcount`,`tcc`.`renewal_amount` AS `renewal_amount`,`tcc`.`last_activation_time` AS `last_activation_time`,`tcc`.`last_click_time` AS `last_click_time`,`tcc`.`renewal_count_of_zero_price_activation_after_1days` AS `renewal_count_of_zero_price_activation_after_1days`,`tcc`.`renewal_amount_of_zero_price_activation_after_1days` AS `renewal_amount_of_zero_price_activation_after_1days`,`tcc`.`renewal_of_zero_price_activation_after_2days` AS `renewal_of_zero_price_activation_after_2days`,`tcc`.`renewal_amount_of_zero_price_activation_after_2days` AS `renewal_amount_of_zero_price_activation_after_2days`,`tcc`.`renewal_of_zero_price_activation_after_more_3days` AS `renewal_of_zero_price_activation_after_more_3days`,`tcc`.`renewal_amount_of_zero_price_activation_after_more_3days` AS `renewal_amount_of_zero_price_activation_after_more_3days`,`tcc`.`service_id` AS `service_id` from ((`tb_live_report` `tcc` left join `vw_adnetwork_campaign` `vwac` on((`tcc`.`adnetwork_campaign_id` = `vwac`.`campaign_id`))) left join `tb_operators` `top` on((`top`.`operator_id` = `tcc`.`operator_id`))) 
  ##############
  ALTER TABLE `cc_sub`.`tb_mobimind_service_config` ADD COLUMN `portal_url` VARCHAR(255) NULL AFTER `cc_operator_id`, ADD COLUMN `cg_url` VARCHAR(255) NULL AFTER `portal_url`; 
  
  ########
  ALTER TABLE `cc_sub`.`tb_ksa_api_trans` CHANGE `token` `token` VARCHAR(255) CHARSET utf8 COLLATE utf8_general_ci NULL AFTER `response_to_caller`, ADD COLUMN `send_to_adnetwork` TINYINT(1) DEFAULT 0 NULL AFTER `token`; 
  #############
  
  ALTER TABLE `cc_sub`.`tb_actel_service_config` ADD COLUMN `service_name` VARCHAR(255) NULL AFTER `operator_id`, ADD COLUMN `price_desc` VARCHAR(100) NULL AFTER `price`; 
 ############
 
 ALTER TABLE `cc_sub`.`tb_mt2_zain_iraq_service_config` ADD COLUMN `sub_msg_template` VARCHAR(255) NULL AFTER `unsub_key`, ADD COLUMN `unsub_msg_template` VARCHAR(255) NULL AFTER `sub_msg_template`; 
 ALTER TABLE `cc_sub`.`tb_mt2_zain_iraq_service_config` ADD COLUMN `product_id` INT NULL AFTER `service_id`; 
 ##########################
 ALTER TABLE `cc_sub`.`tb_mt2_ksa_service_config` ADD COLUMN `alert_sms_template` VARCHAR(255) NULL AFTER `lp_img`; 
 
  
  
  ALTER TABLE `cc_sub`.`tb_mobivate_service_config` ADD COLUMN `lp_img` VARCHAR(255) NULL AFTER `sms_value`; 
   ALTER TABLE `cc_sub`.`tb_mobivate_service_config` ADD COLUMN `term_condition_url` VARCHAR(255) NULL AFTER `lp_img`; 
   
   ########
   
   ALTER TABLE `cc_sub`.`tb_worldplay_service_config` ADD COLUMN `term_condition_page` VARCHAR(255) NULL AFTER `portal_url`; 
  #######################
  
  
  ALTER TABLE `cc_sub`.`tb_live_report` ADD COLUMN `mode` VARCHAR(100) NULL AFTER `subscription_failed`; 
  ALTER TABLE `cc_sub`.`tb_live_report` DROP INDEX `unique_key`, ADD UNIQUE INDEX `unique_key` (`report_date`, `adnetwork_campaign_id`, `action_hours`, `operator_id`, `service_id`, `mode`); 
 ALTER TABLE `cc_sub`.`tb_live_report` ADD COLUMN `pin_validation_count` INT DEFAULT 0 NULL AFTER `mode`; 
  ALTER TABLE `cc_sub`.`tb_live_report` ADD COLUMN `pin_send_count` INT DEFAULT 0 NULL AFTER `mode`; 
  
  ALTER TABLE `cc_sub`.`tb_subscribers_reg` ADD COLUMN `mode` VARCHAR(200) NULL AFTER `param3`;
  #####################
  ALTER TABLE `cc_sub`.`tb_mt2_uae_service_config` ADD COLUMN `secret_key` VARCHAR(100) NULL AFTER `mnc`; 
   ALTER TABLE `cc_sub`.`tb_mt2_uae_service_config` ADD COLUMN `lp_page` VARCHAR(100) NULL AFTER `portal_url`;
   #########
   ALTER TABLE `cc_sub`.`tb_mt2_uae_notification` ADD COLUMN `my_action` VARCHAR(100) NULL AFTER `id`;
    ALTER TABLE `cc_sub`.`tb_mt2_uae_service_config` ADD COLUMN `mt2_op_id` VARCHAR(100) NULL AFTER `client_id`;
    ALTER TABLE `cc_sub`.`tb_mt2_uae_service_config` ADD COLUMN `sms_user` VARCHAR(100) NULL AFTER `sms_url`, ADD COLUMN `sms_password` VARCHAR(100) NULL AFTER `sms_user`; 
 ######
 
 ALTER TABLE `cc_sub`.`tb_operators` ADD COLUMN `perhour_conversion_capping` INT NULL AFTER `country_id`, ADD COLUMN `daily_conversion_capping` INT NULL AFTER `perhour_conversion_capping`; 
  ALTER TABLE `cc_sub`.`tb_adnetwork_operator_config` DROP COLUMN `reverse_status`, DROP COLUMN `reverse_skip_number`, ADD COLUMN `perhour_conversion_capping` INT NULL AFTER `zero_price_activation_send`, ADD COLUMN `daily_conversion_capping` INT NULL AFTER `perhour_conversion_capping`; 
  ALTER TABLE `cc_sub`.`tb_product` ADD COLUMN `product_wise_capping_per_day` INT NULL AFTER `product_name`, ADD COLUMN `product_wise_hourly_click_capping_redirect_to_cg` INT NULL AFTER `product_wise_capping_per_day`; 

  ##########
   ALTER TABLE `cc_sub`.`tb_mk_malaysia_config` ADD COLUMN `mt_billing_message_template` TEXT NULL AFTER `subscription_cycle`, ADD COLUMN `mt_welcome_message_template` TEXT NULL AFTER `mt_billing_message_template`, ADD COLUMN `mt_renewal_message_template` TEXT NULL AFTER `mt_welcome_message_template`; 
   
  
   
   