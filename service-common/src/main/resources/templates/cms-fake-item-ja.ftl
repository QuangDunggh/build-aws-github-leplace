<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<body style="margin: 0; padding: 0;">

<div
    style="margin: 20px 20px; padding:5px; font-size: 10pt; line-height: 1.5; font-family:'MS PGothic', 'Helvetica Neue', 'Helvetica', Arial, sans-serif;">
  <p><i style="color:black;">${userName}</i>様</p>
  <p>
    このたびは${serviceName}への出品リクエストを頂きありがとうございました。
    <br/>
    ご発送いただいたアイテムですが、${serviceName}でのお取り扱いができないものが含まれていおりましたので、サービス利用規約に基づきご登録いただいているご住所に返品をさせていただきます。
  </p>
    返品アイテムの詳細
    <br/>
    <br/>
    【リクエスト日時】<i style="color:black;">${requestTime}</i>
    <br/>
    【リクエストID】<i style="color:black;">${requestId}</i>
    <br/>
    【アイテムID】<a href="${itemUrl}" style="text-decoration: none;"><i style="color:black;">${itemId}</i></a>
    <br/>
    【ブランド】<i style="color:black;">${brandName}</i>
    <br/>
    【アイテム名】<i style="color:black;">${itemName}</i>
    <br/>
    <br/>

    上記に関するお問い合わせは、${serviceName}のお問合せフォームよりご連絡ください。
    <br/>
    <br/>
	<hr>

	<p>【お問い合わせ先】<a href="${link}"
                  style="text-decoration: none;"><i style="color:blue;">${link}</i></a></p>

	<hr>

	<p>※このメールアドレスは配信専用となっております。本メールに返信いただいても、お問い合わせにはお答えできませんのでご了承ください。</p>
</div>

</body>
</html>