<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<body style="margin: 0; padding: 0;">

<div
    style="margin: 20px 20px; padding:5px; font-size: 10pt; line-height: 1.5;
    font-family:'MS PGothic', 'Helvetica Neue', 'Helvetica', Arial, sans-serif;">
  <p><i style="color:black;">${userName}</i>様</p>
  <p>
    いつも ${serviceName} をご利用いただき、ありがとうございます。
  </p>
  <p>
    下記のアイテムの購入が完了したことをお知らせいたします。
  </p>
  <p>
  	【注文ID】<i style="color:black;">${orderId}</i>
    <br/>
    【アイテムID】ITEM 1：<a href="${itemUrl}" style="text-decoration: none;"><i style="color:black;">${itemId}</i></a>
    <br/>
    【ブランド名】<i style="color:black;">${brandName}</i>
    <br/>
    【アイテム名】<i style="color:black;">${itemName}</i>
    <br/>
    【出品者】<i style="color:black;">${sellerName}</i>
    <br/>
    【お支払い金額】¥<i style="color:black;">${paymentAmount}</i>
    <br/>
    【購入日時】<i style="color:black;">${orderDatetime}</i>
    <br/>
    【配送料負担】<i style="color:black;">${deliveryFeesBearer}負担</i>
    <br/>
    【お届け予定日】 <i style="color:black;">${estimatedDatetimeOfArrival}</i>
    <br/>
    【送付先】<i style="color:black;">${recipientName}</i><i>様</i>
    <br/>
    【配送先住所】<i style="color:black;">${recipientAddress}</i>
    <br/>

    <br/>
    【配送についての重要なご案内】<br/>
    ヤマト運輸での配送となります。<br/>
    15:00までにご購入いただいたアイテムは当日中に配送手配をさせていただきます。(土日祝日を除く)<br/>
    配送手配完了後、マイページの購入履歴画面に表示される配送伝票番号で配送状況をご確認いただけます。<br/>
    *アイテムサイズによっては発送が遅れる場合がございます。到着まで今しばらくお待ちください。
  </p>
	<hr>

	<p>
	  【お問い合わせ先】<a href="${contactUrl}"
    style="text-decoration: none;"><i style="color:blue;">${contactUrl}</i></a>
  </p>

	<hr>

	<p>※このメールアドレスは配信専用となっております。本メールに返信いただいても、お問い合わせにはお答えできませんのでご了承ください。</p>
</div>

</body>
</html>