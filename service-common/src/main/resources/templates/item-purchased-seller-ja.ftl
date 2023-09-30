<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<body style="margin: 0; padding: 0;">

<div
    style="margin: 20px 20px; padding:5px; font-size: 10pt; line-height: 1.5; font-family:'MS PGothic', 'Helvetica Neue', 'Helvetica', Arial, sans-serif;">
  <p><i style="color:black;">${userName}</i>様</p>
  <p>
    いつも ${serviceName} をご利用いただき、ありがとうございます。
  </p>
  <p>
    下記の出品されているアイテムが購入されたことをお知らせいたします。
  </p>
  <p>
    【アイテムID】ITEM 1：<a href="${itemUrl}" style="text-decoration: none;"><i style="color:black;">${itemId}</i></a>
    <br/>
    【ブランド名】<i style="color:black;">${brandName}</i>
    <br/>
    【アイテム名】<i style="color:black;">${itemName}</i>
    <br/>
    【購入金額】¥<i style="color:black;">${itemPrice}</i>
    <br/>
    【購入日時】<i style="color:black;">${orderDatetime}</i>
    <br/>
    【配送料負担】<i style="color:black;">${deliveryFeesBearer}負担</i>
    <br/>
    【入金予定金額】¥<i style="color:black;">${depositedAmount}</i>
    <br/>

    <br/>
    ${serviceName}の手数料等を引いた上記の入金予定金額がご指定の口座に${estimatedDeliveryDays}営業日程度で振り込まれます。
    </p>
	<hr>

	<p>【お問い合わせ先】<a href="${contactUrl}"
    style="text-decoration: none;"><i style="color:blue;">${contactUrl}</i></a></p>

	<hr>

	<p>※このメールアドレスは配信専用となっております。本メールに返信いただいても、お問い合わせにはお答えできませんのでご了承ください。</p>
</div>

</body>
</html>