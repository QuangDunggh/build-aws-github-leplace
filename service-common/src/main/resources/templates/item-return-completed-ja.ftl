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
    ご返品依頼をいただいた下記のアイテムの配送が完了したことをお知らせいたします。
  </p>
  <p>
    【アイテムID】ITEM 1：<a href="${itemUrl}" style="text-decoration: none;"><i style="color:black;">${itemId}</i></a>
    <br/>
    【ブランド名】<i style="color:black;">${brandName}</i>
    <br/>
    【アイテム名】<i style="color:black;">${itemName}</i>
    <br/>
    【送付先】<i style="color:black;">${recipientName}</i><i>様 </i>
    <br/>
    【配送先住所】<i style="color:black;">${recipientAddress}</i>
    <br/>
    <br/>
    配送状況をご確認いただく場合は、下記の配送伝票番号をお控えの上、配送会社様にご確認ください。
    <br/>
    <br/>
    【配送伝票番号】<i style="color:black;">${deliveryTrackingNumber}</i>
    <br/>
    【配送状況の確認ページ】<i style="color:black;">${deliveryStatusConfirmationPageUrl}</i>
    <br/>
    <br/>
    </p>
	<hr>

	<p>【お問い合わせ先】<a href="${contactUrl}"
    style="text-decoration: none;"><i style="color:blue;">${contactUrl}</i></a></p>

	<hr>

	<p>※このメールアドレスは配信専用となっております。本メールに返信いただいても、お問い合わせにはお答えできませんのでご了承ください。</p>
</div>

</body>
</html>