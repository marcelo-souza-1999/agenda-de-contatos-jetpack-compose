package com.marcelos.agendadecontatos.presentation.components

import android.content.res.Configuration
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.marcelos.agendadecontatos.R
import com.marcelos.agendadecontatos.domain.model.ContactsViewData
import com.marcelos.agendadecontatos.presentation.theme.ContactsAgendaTheme
import com.marcelos.agendadecontatos.presentation.theme.TypographyTitle
import com.marcelos.agendadecontatos.presentation.ui.navigation.Routes
import com.marcelos.agendadecontatos.utils.formattedPhone
import java.io.File

@Composable
fun ListContactsItem(
    contactsData: ContactsViewData,
    navController: NavController,
    onDelete: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                dimensionResource(R.dimen.size_12)
            ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = dimensionResource(R.dimen.size_8)
        ),
        shape = RoundedCornerShape(
            dimensionResource(R.dimen.size_20)
        )
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            val (imageContact, txtName, txtAge, txtPhone, imgClickUpdate, imgClickDelete) = createRefs()
            val margin8 = dimensionResource(id = R.dimen.size_8)
            val margin16 = dimensionResource(id = R.dimen.size_16)
            val margin20 = dimensionResource(id = R.dimen.size_20)
            val margin80 = dimensionResource(id = R.dimen.size_80)

            Image(
                painter = rememberImagePainter(contactsData.imagePath),
                contentDescription = null,
                modifier = Modifier
                    .clip(CircleShape)
                    .constrainAs(imageContact) {
                        top.linkTo(parent.top, margin20)
                        start.linkTo(parent.start, margin8)
                        width = Dimension.value(margin80)
                        height = Dimension.value(margin80)
                    },
                contentScale = ContentScale.Crop
            )

            CreateText(
                text = stringResource(
                    R.string.contact_name_format,
                    contactsData.name, contactsData.surname
                ),
                modifier = Modifier
                    .padding(dimensionResource(R.dimen.size_8))
                    .constrainAs(txtName) {
                        start.linkTo(imageContact.end)
                        top.linkTo(imageContact.top)
                    }
            )

            CreateText(
                text = stringResource(R.string.contact_age_format, contactsData.age),
                modifier = Modifier
                    .padding(start = dimensionResource(R.dimen.size_8))
                    .constrainAs(txtAge) {
                        start.linkTo(txtName.start)
                        top.linkTo(txtName.bottom, margin8)
                    }
            )

            CreateText(
                text = stringResource(
                    R.string.contact_phone_format,
                    contactsData.phone.formattedPhone()
                ),
                modifier = Modifier
                    .padding(start = dimensionResource(R.dimen.size_8))
                    .constrainAs(txtPhone) {
                        start.linkTo(txtAge.start)
                        top.linkTo(txtAge.bottom, margin = margin16)
                        bottom.linkTo(parent.bottom, margin16)
                    }
            )

            CreateIconButton(
                iconRes = R.drawable.ic_edit_contact,
                modifier = Modifier
                    .wrapContentSize()
                    .clickable {
                        navController.navigate(
                            route = "${Routes.UpdateContact.route}/${contactsData.id}"
                        )
                    }
                    .constrainAs(imgClickUpdate) {
                        start.linkTo(txtPhone.end, margin8)
                        bottom.linkTo(txtPhone.bottom)
                    }
            )

            CreateIconButton(
                iconRes = R.drawable.ic_delete_contact,
                modifier = Modifier
                    .wrapContentSize()
                    .clickable {
                        contactsData.id?.let { onDelete(it) }
                    }
                    .constrainAs(imgClickDelete) {
                        start.linkTo(imgClickUpdate.end, margin8)
                        bottom.linkTo(txtPhone.bottom)
                    }
            )
        }
    }
}

@Composable
private fun rememberImagePainter(imagePath: String?): Painter {
    val imageBitmap = imageBitmapFromPath(imagePath)
    return imageBitmap?.let { BitmapPainter(it) } ?: painterResource(R.drawable.ic_contact_photo)
}

@Composable
private fun imageBitmapFromPath(imagePath: String?): ImageBitmap? {
    return imagePath?.let {
        val file = File(it)
        val bitmap = BitmapFactory.decodeFile(file.absolutePath)
        bitmap?.asImageBitmap()
    }
}

@Composable
private fun CreateText(
    text: String,
    modifier: Modifier
) {
    Text(
        text = text,
        style = TypographyTitle.titleLarge,
        modifier = modifier
    )
}

@Composable
private fun CreateIconButton(
    iconRes: Int,
    modifier: Modifier
) {
    Image(
        imageVector = ImageVector.vectorResource(iconRes),
        contentDescription = null,
        modifier = modifier,
        contentScale = ContentScale.Crop
    )
}

@Preview(
    showBackground = true,
    showSystemUi = false,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
internal fun PreviewListContactsItem() {
    ContactsAgendaTheme {
        ListContactsItem(
            navController = rememberNavController(),
            contactsData = ContactsViewData(
                name = "Marcelo",
                surname = "Souza",
                age = 30,
                phone = "12996259393",
                imagePath = null
            ),
            onDelete = {
                Log.d("DeleteContact", "Ação de deletar contato")
            }
        )
    }
}