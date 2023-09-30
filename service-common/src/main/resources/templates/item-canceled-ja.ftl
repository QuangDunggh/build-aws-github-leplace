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
    下記アイテムの出品のキャンセルを承りましたことをお知らせいたします。
  </p>
  <p>
    【アイテムID】ITEM 1<a href="${itemUrl}" style="text-decoration: none;"><i style="color:black;">${itemId}</i></a>
    <br/>
    【キャンセル日時】<i style="color:black;">${orderDatetime}</i>
    <br/>
    <br/>
    またのご利用をお待ちしております。<br/>
	アイテムの配送手配状況は、ご登録いただいているメールアドレスに後日ご案内させていただきます。
    </p>
	<hr>

	<p>【お問い合わせ先】<a href="${contactUrl}"
    style="text-decoration: none;"><i style="color:black;">${contactUrl}</i></a></p>

	<p>───────────────────────────────</p>

	<p>※このメールアドレスは配信専用となっております。本メールに返信いただいても、お問い合わせにはお答えできませんのでご了承ください。</p>
</div>

</body>
</html>