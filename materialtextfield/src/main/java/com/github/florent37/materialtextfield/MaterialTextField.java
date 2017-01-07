package com.github.florent37.materialtextfield;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v4.view.ViewPropertyAnimatorUpdateListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;


public class MaterialTextField extends FrameLayout {


    protected TextView label;
    protected View card;
    protected ImageView image;
    protected EditText editText;
    protected ViewGroup editTextLayout;

    protected int labelTopMargin = -1;
    protected boolean expanded = false;

    protected int ANIMATION_DURATION = -1;
    protected boolean OPEN_KEYBOARD_ON_FOCUS = true;
    protected int labelColor = -1;
    protected int imageDrawableId = -1;
    protected int cardCollapsedHeight = -1;

    protected float reducedScale = 0.2f;

    public MaterialTextField(Context context) {
        super(context);
    }

    public MaterialTextField(Context context, AttributeSet attrs) {
        super(context, attrs);
        handleAttributes(context,attrs);
    }

    public MaterialTextField(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        handleAttributes(context, attrs);
    }

    public void toggle() {
        if (expanded) {
            reduce();
        } else {
            expand();
        }
    }

    public void reduce() {
        if (expanded) {

            final int heightInitial = getContext().getResources().getDimensionPixelOffset(com.github.florent37.materialtextfield.R.dimen.mtf_cardHeight_final);
            ViewCompat.animate(label)
                    .alpha(1)
                    .scaleX(1)
                    .scaleY(1)
                    .translationY(0)
                    .setDuration(ANIMATION_DURATION);

            ViewCompat.animate(image)
                    .alpha(0)
                    .scaleX(0.4f)
                    .scaleY(0.4f)
                    .setDuration(ANIMATION_DURATION);

            ViewCompat.animate(editText)
                    .alpha(0f)
                    /*.setUpdateListener(new ViewPropertyAnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(View view) {
                            float value = ViewCompat.getAlpha(view); //percentage
                            card.getLayoutParams().height = (int) (value * (heightInitial - cardCollapsedHeight) + cardCollapsedHeight);
                            card.requestLayout();
                        }
                    })*/
                    .setDuration(ANIMATION_DURATION);

            ViewCompat.animate(card)
                    .scaleY(reducedScale)
                    .setDuration(ANIMATION_DURATION);


            if (OPEN_KEYBOARD_ON_FOCUS) {
                ((InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            }
            editText.clearFocus();

            expanded = false;
        }
    }

    public void expand() {
        if (!expanded) {

            ViewCompat.animate(editText)
                    .alpha(1f)
                    .setDuration(ANIMATION_DURATION);

            ViewCompat.animate(card)
                    .scaleY(1f)
                    .setDuration(ANIMATION_DURATION);

            ViewCompat.animate(label)
                    .alpha(0.4f)
                    .scaleX(0.7f)
                    .scaleY(0.7f)
                    .translationY(-labelTopMargin)
                    .setDuration(ANIMATION_DURATION);

            ViewCompat.animate(image)
                    .alpha(1f)
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(ANIMATION_DURATION);

            editText.requestFocus();
            if (OPEN_KEYBOARD_ON_FOCUS) {
                ((InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
            }

            expanded = true;
        }
    }

    public View getCard() {
        return card;
    }

    public TextView getLabel() {
        return label;
    }

    public ImageView getImage() {
        return image;
    }

    public EditText getEditText() {
        return editText;
    }

    public ViewGroup getEditTextLayout() {
        return editTextLayout;
    }

    public boolean isExpanded() {
        return expanded;
    }

    protected void handleAttributes(Context context, AttributeSet attrs) {
        try {
            TypedArray styledAttrs = context.obtainStyledAttributes(attrs, com.github.florent37.materialtextfield.R.styleable.MaterialTextField);

            {
                ANIMATION_DURATION = styledAttrs.getInteger(com.github.florent37.materialtextfield.R.styleable.MaterialTextField_mtf_animationDuration, 400);
            }
            {
                OPEN_KEYBOARD_ON_FOCUS = styledAttrs.getBoolean(com.github.florent37.materialtextfield.R.styleable.MaterialTextField_mtf_openKeyboardOnFocus, false);
            }
            {
                labelColor = styledAttrs.getColor(com.github.florent37.materialtextfield.R.styleable.MaterialTextField_mtf_labelColor, -1);
            }
            {
                imageDrawableId = styledAttrs.getResourceId(com.github.florent37.materialtextfield.R.styleable.MaterialTextField_mtf_image, -1);
            }
            {
                cardCollapsedHeight = styledAttrs.getDimensionPixelOffset(com.github.florent37.materialtextfield.R.styleable.MaterialTextField_mtf_cardCollapsedHeight, context.getResources().getDimensionPixelOffset(com.github.florent37.materialtextfield.R.dimen.mtf_cardHeight_initial));
            }

            styledAttrs.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected EditText findEditTextChild() {
        if (getChildCount() > 0 && getChildAt(0) instanceof EditText) {
            return (EditText) getChildAt(0);
        }
        return null;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        editText = findEditTextChild();
        if (editText == null) {
            return;
        }

        addView(LayoutInflater.from(getContext()).inflate(com.github.florent37.materialtextfield.R.layout.mtf_layout, this, false));

        editTextLayout = (ViewGroup) findViewById(com.github.florent37.materialtextfield.R.id.mtf_editTextLayout);
        removeView(editText);
        editTextLayout.addView(editText);

        label = (TextView) findViewById(com.github.florent37.materialtextfield.R.id.mtf_label);
        ViewCompat.setPivotX(label, 0);
        ViewCompat.setPivotY(label, 0);

        if (editText.getHint() != null) {
            label.setText(editText.getHint());
            editText.setHint("");
        }

        card = findViewById(com.github.florent37.materialtextfield.R.id.mtf_card);

        final int expandedHeight = getContext().getResources().getDimensionPixelOffset(com.github.florent37.materialtextfield.R.dimen.mtf_cardHeight_final);
        final int reducedHeight = cardCollapsedHeight;

        reducedScale = (float) (reducedHeight * 1.0 / expandedHeight);
        ViewCompat.setScaleY(card, reducedScale);
        ViewCompat.setPivotY(card, expandedHeight);

        image = (ImageView) findViewById(com.github.florent37.materialtextfield.R.id.mtf_image);
        ViewCompat.setAlpha((View) image, 0);
        ViewCompat.setScaleX(image, 0.4f);
        ViewCompat.setScaleY(image, 0.4f);

        ViewCompat.setAlpha(editText, 0f);
        editText.setBackgroundColor(Color.TRANSPARENT);

        labelTopMargin = FrameLayout.LayoutParams.class.cast(label.getLayoutParams()).topMargin;

        customizeFromAttributes();

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle();
            }
        });

    }

    protected void customizeFromAttributes() {
        if (labelColor != -1) {
            this.label.setTextColor(labelColor);
        }
        if (imageDrawableId != -1) {
            this.image.setImageDrawable(getContext().getResources().getDrawable(imageDrawableId));
        }
    }



}
