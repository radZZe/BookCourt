import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.pager.PageSize.Fill.calculateMainAxisPageSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.bookcourt.R
import com.example.bookcourt.models.metrics.DataClickMetric
import com.example.bookcourt.ui.recommendation.*
import com.example.bookcourt.ui.theme.MainBgColor
import com.example.bookcourt.utils.Buttons
import com.example.bookcourt.utils.Constants
import com.example.bookcourt.utils.Screens
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheetCustom(
    price:Int,
    controller:BottomSheetController,
    modifier: Modifier,
    sheetContent:@Composable () -> Unit,
    onExpanding:()->Unit,
    onCollapsing:()->Unit,
    thresholdConfig: (Float, Float) -> ThresholdConfig = { _, _ -> FractionalThreshold(0.2f) }
) {
        var scrollState = rememberScrollState()
        if(!controller.isExpanded.value){
            val scope = rememberCoroutineScope()
            scope.apply {
                launch {
                    scrollState.scrollTo(0)
                }
            }

        }
        Box(modifier =
        modifier
            .fillMaxWidth()
            .height(controller.sheetHeight.value.dp)
            .draggableBottomSheet(
                controller = controller,
                thresholdConfig = thresholdConfig,
                onCollapsing = onCollapsing,
                onExpanding = onExpanding
            )
            .verticalScroll(scrollState,
                enabled = controller.isExpanded.value
            )
        ){
            sheetContent()
        }



}
