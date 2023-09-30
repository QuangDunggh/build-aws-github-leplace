package com.laplace.api.common.constants;

public final class CSVHeaderConstants {

  public static final String[] PACKAGE_CSV_HEADERS = new String[]{CompanyInfoColumns.COMPANY_NAME,
      CompanyInfoColumns.EMAIL, CompanyInfoColumns.SITE_URL, CompanyInfoColumns.API_END_POINT,
      CompanyInfoColumns.API_CLIENT, CompanyInfoColumns.API_CREDENTIAL, CompanyInfoColumns.API_KEY};
  public static final String[] AFFILIATE_CSV_HEADERS = new String[]{CompanyInfoColumns.COMPANY_NAME,
      CompanyInfoColumns.EMAIL, CompanyInfoColumns.SITE_URL};

  public static final String[] HOTEL_CSV_HEADERS = new String[]{HotelInfoHeaders.CODE,
      HotelInfoHeaders.NAME, HotelInfoHeaders.POSTAL_CODE, HotelInfoHeaders.STATE_ID,
      HotelInfoHeaders.PREFECTURES, HotelInfoHeaders.CITY,
      HotelInfoHeaders.LINE1, HotelInfoHeaders.LINE2, HotelInfoHeaders.TEL1};
}
