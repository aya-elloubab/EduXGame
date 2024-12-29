package ma.ensaj.edugame.viewModel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class SignUpViewModelFactory implements ViewModelProvider.Factory {
    private final Context context;

    public SignUpViewModelFactory(Context context) {
        this.context = context.getApplicationContext(); // Use ApplicationContext to avoid leaking Activity
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(SignUpViewModel.class)) {
            return (T) new SignUpViewModel(context);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
