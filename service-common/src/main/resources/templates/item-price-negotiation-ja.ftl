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
  ${negotiatorName}さんから、あなたが出品中の商品「${itemName}」に対して値下げ交渉が届きました。
  </p>
  <p>
  現在の金額：¥${currentPrice}
  <br/>
  交渉金額：¥${discountRequestPrice}
  </p>
  <p>
  下記のURLより、該当する商品の詳細情報を確認できます。
  <p>
  URL：${sellClosetUrl}
  </p>
	<hr>
	<p>【お問い合わせ先】<a href="${contactUrl}"
    style="text-decoration: none;"><i style="color:blue;">${contactUrl}</i></a></p>
	<hr>

	<p>※このメールアドレスは配信専用となっております。本メールに返信いただいても、お問い合わせにはお答えできませんのでご了承ください。</p>
</div>

</body>
</html>