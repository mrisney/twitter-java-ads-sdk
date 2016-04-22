[![license](http://img.shields.io/badge/license-MIT-red.svg?style=flat)](https://raw.githubusercontent.com/mrisney/twitter-java-ads-sdk/master/LICENSE.md)
[![Build Status](https://travis-ci.org/mrisney/twitter-java-ads-sdk.svg?branch=master)](https://travis-ci.org/mrisney/twitter-java-ads-sdk)
[![Coverage Status](https://coveralls.io/repos/github/mrisney/twitter-java-ads-sdk/badge.svg?branch=master)](https://coveralls.io/github/mrisney/twitter-java-ads-sdk?branch=master)

###### Introduction
This is a Java library for   [Twitter Ads API](https://dev.twitter.com/ads/overview).

###### Quick Start

``` java

import com.steelhouse.twitter.ads.Account;
import com.steelhouse.twitter.ads.TwitterAdsClient;
import com.steelhouse.twitter.ads.campaign.Campaign;


public class QuickStart {

  static String CONSUMER_KEY = "your consumer key";
  static String CONSUMER_SECRET = "your consumer secret";
  static String ACCESS_TOKEN = "access token";
  static String ACCESS_SECRET = "access token secret";
  static String ACCOUNT_ID = "account id";

  public static void main(String[] args) throws Exception {

    // initialize the twitter ads api client
    TwitterAdsClient client = new TwitterAdsClient(CONSUMER_KEY,
                                                   CONSUMER_SECRET,
                                                   ACCESS_TOKEN,
                                                   ACCESS_SECRET);

    client.setTrace(false);
    Account account = client.getAccount(ACCOUNT_ID);
    String fundingInstrumentId = account.getFundingInstruments().get(0).getId();

    Date now = Date.from(Instant.now());
    Calendar cal = new GregorianCalendar();
    cal.add(Calendar.DAY_OF_MONTH, +7);
    Date weekFromToday = cal.getTime();


    // create your campaign
    Campaign campaign = Campaign.builder()
    			.account(account)
    			.funding_instrument_id(fundingInstrumentId)
    			.currency("US")
    			.daily_budget_amount_local_micro(1000000)
    			.total_budget_amount_local_micro(7000000)
    			.name("My New Campaign")
    			.paused(false)
    			.start_time(now)
    			.end_time(weekFromToday)
    			.build();

    campaign.save();

    System.out.println("campaign newly created : " + campaign.toString());

    // loop through campaigns ...
    for (Campaign c : account.getCampaigns()) {
      System.out.println(c.toString());
    }
}
```
